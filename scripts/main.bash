export SSHPASS='swarch'

while getopts 's':'c':'h' flag
do 
        case "${flag}" in
                s) server_id=${OPTARG}
                    ;;
                c) client_ids=${OPTARG}
                    ;;
                h) echo "Usage: ./experiment.bash -s <server id> -c <client ids>"
                    echo 'For example: ./experiment.bash -s 1 -c 2,3,4'
                    echo 'server id: number of the server, if 21 then xhgrid21'
                    echo 'client ids: ids of the clients, if 1,2,3 then xhgrid1, xhgrid2, xhgrid3'
                    exit 0
                    ;;
                *) echo "Invalid option -${flag}"
                    ;;
        esac
done

required_args=(server_id client_ids)
flag_names=(s c)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

server_hostname="xhgrid$server_id"
clients_array=(${client_ids//,/ })

echo 'Running experiment with the following parameters:'
server_ip_address=$(getent hosts $server_hostname | awk '{ print $1 }')
echo "Server hostname: '$server_hostname' ip address: '$server_ip_address'"
echo "Clients:"

for id in "${clients_array[@]}"; do
    client_hostname="xhgrid$id"
    sshpass -p swarch ssh swarch@$client_hostname 'killall java' > /dev/null 2>&1
    client_ip_address=$(getent hosts $client_hostname | awk '{ print $1 }')
    echo "*      $client_hostname:' ip address: '$client_ip_address'"
done

echo 'Starting server...'
server_out_file="logs/server-$server_hostname-output.log"
bash scripts/server/deploy.bash -s $server_id -b feat/multithread >> $server_out_file 2>&1 &
sleep 10
echo "Server started, deploying clients..."

bash scripts/experiment.bash -c $client_ids -s $server_ip_address -y true
exit 0