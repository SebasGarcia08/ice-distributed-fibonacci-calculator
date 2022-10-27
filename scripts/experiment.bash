#!/bin/bash
confirmed=false
while getopts 'c':'s':'y':'h' flag
do 
        case "${flag}" in
                c) client_ids=${OPTARG}
                    ;;
                s) server_ip_address=${OPTARG}
                    ;;
                y) confirmed=true
                    ;;
                h) echo "Usage: ./experiment.bash -n <num_clients> -f <max_fib> -w <max_wait> -m <max_heap>"
                    echo 'num_clients: number of clients to request the server'
                    echo 'max_fib: maximum number to calculate the fibonacci sequence'
                    echo 'max_wait: maximum time to wait among equests'
                    echo 'max_heap: maximum heap size for the java virtual macihne for the server'
                    exit 0
                    ;;
                *) echo "Invalid option -${flag}"
                    ;;
        esac
done

required_args=(client_ids server_ip_address)
flag_names=(c s)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

clients_array=(${client_ids//,/ })
echo 'Running experiment with the following parameters:'
echo "Clients: $client_ids"
echo "Server ip address: $server_ip_address"

if [ "$confirmed" = false ]; then
        echo 'Are you sure you want to continue? [y/n]: '
        read -r confirmed
        if [ "$confirmed" != 'y' ]; then
                echo 'Aborting...'
                exit 0
        fi
fi

for id in "${clients_array[@]}"; do
    hostname="xhgrid$id"
    while true;
    do
      ping -c1 $hostname > /dev/null
      if [ $? -eq 0 ]
      then 
        echo "$hostname is up, deploying..."
        bash scripts/client/deploy.bash -s $server_ip_address -b feat/multithread -c $id -p 9777 >> logs/$hostname-output.log 2>&1 &
        break
      else
        echo "$hostname is down"
        break
      fi
    done
done
