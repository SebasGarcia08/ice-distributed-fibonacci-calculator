#!/bin/bash
confirmed=false
while getopts 'i':'n':'s':'y':'h' flag
do 
        case "${flag}" in
                i) start_idx=${OPTARG}
                    ;;
                n) num_clients=${OPTARG}
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

required_args=(start_idx num_clients server_ip_address)
flag_names=(i n s)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

echo 'Running experiment with the following parameters:'
echo "Start index: $start_idx"
echo "Number of clients: $num_clients"
echo "Server ip address: $server_ip_address"

if [ "$confirmed" = false ]; then
        echo 'Are you sure you want to continue? [y/n]: '
        read -r confirmed
        if [ "$confirmed" != 'y' ]; then
                echo 'Aborting...'
                exit 0
        fi
fi

for id in $(seq $start_idx $num_clients); do
    hostname="xhgrid$id"
    while true;
    do
      ping -c1 $hostname > /dev/null
      if [ $? -eq 0 ]
      then 
        echo "$hostname is up, deploying..."
        bash scripts/client/deploy.bash -s $server_ip_address -b feat/multithread -c $id -p 901$id > output-$id.log &
        break
      fi
    done
done
