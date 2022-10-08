#!/bin/bash
while getopts 'n':'f':'w':'m':'h' flag
do 
        case "${flag}" in
                n) num_clients=${OPTARG}
                    ;;
                f) max_fib=${OPTARG}
                    ;;
                w) max_wait=${OPTARG}
                    ;;
                m) max_heap=${OPTARG}
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

required_args=(num_clients max_fib max_wait max_heap)
flag_names=(n f w m)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

echo 'Running experiment with the following parameters:'
echo "Number of clients: $num_clients"
echo "Max fib: $max_fib"
echo "Max wait: $max_wait"
echo "Max heap: $max_heap"

if [ "$confirmed" = false ]; then
        echo 'Are you sure you want to continue? (y/n)'
        read -r confirmed
        if [ "$confirmed" != 'y' ]; then
                echo 'Aborting...'
                exit 0
        fi
fi

echo "Compressing files..."
zip -r sebas-aleja.zip .
echo 'Done.'

echo "Installing sshpass..."
apt-get install sshpass
echo 'Done.'

echo 'Compiling...'
#./gradlew build

for i in $(seq 1 $num_clients); do
    hostname="xhgrid$i"
    echo "Copying to $hostname"
    sshpass -p "swarch" scp sebas-aleja.zip swarch@$hostname:~/ 
    echo 'Done.'
done
