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
server_ip_address=$(getent hosts $server_hostname | awk '{ print $1 }')
echo "Server hostname: '$server_hostname' ip address: '$server_ip_address'"
bash scripts/copy-files.bash $server_hostname

echo 'Compiling...'
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$server_hostname 'cd sebas-aleja && ./gradlew build'

echo 'Running server'
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$server_hostname "cd sebas-aleja && java -jar server/build/libs/server.jar --Ice.Default.Host=$server_ip_address"

## Deploy clients
exit 0
echo "Clients: "
clients_array=(${client_ids//,/ })
for client_id in "${clients_array[@]}"; do
        client_hostname="xhgrid$client_id"
        bash scripts/copy-files.bash $client_hostname
        echo "$client_hostname"
done
