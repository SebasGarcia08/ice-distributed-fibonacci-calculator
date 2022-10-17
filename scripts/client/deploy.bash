export SSHPASS='swarch'

while getopts c:s:b:h flag
do 
        case "${flag}" in
                c) client_id=${OPTARG}
                    ;;
                s) server_id=${OPTARG}
                    ;;
                b) branch=${OPTARG}
                    ;;
                h) echo "Usage: ./experiment.bash -s <server id> -b <branch>"
                    echo 'For example: ./experiment.bash -s 1 -b feat/multithread'
                    echo 'server id: number of the server, if 21 then xhgrid21'
                    echo 'bramch: branch to deploy, should be a valid branch from https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator repo ' 
                    exit 0
                    ;;
                *) echo "Invalid option -${flag}"
                    ;;
        esac
done

required_args=(client_id server_id branch)
flag_names=(c s b)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

server_hostname="xhgrid$server_id"
client_hostname="xhgrid$client_id"

server_ip_address=$(getent hosts $server_hostname | awk '{ print $1 }')
client_ip_address=$(getent hosts $client_hostname | awk '{ print $1 }')
client_callback_port="9${client_id}77"
client_callback_endpoints="default -h $client_ip_address -p $client_callback_port" 

cmd1="cd $repo_dir"
cmd2="java -jar client/build/libs/client.jar --Ice.Default.Host=${server_ip_address} --Callback.Endpoints=${client_callback_endpoints}"
launch_cmd="${cmd1} && ${cmd2}"

repo_url="https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator.git"
repo_dir="ice-distributed-fibonacci-calculator"

echo "Server hostname: '$server_hostname' ip address: '$server_ip_address'"
echo "Client hostname: '$client_hostname' ip address: '$client_ip_address'"
echo "Repo url: '$repo_url', clone dir: '$repo_dir' branch: '$branch'"
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$client_hostname "rm -rf $repo_dir; git clone --branch $branch $repo_url "

echo 'Compiling...'
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$client_hostname "cd $repo_dir && ./gradlew build"

echo "Running client with ip address: $client_ip_address (callback on port $client_callback_port)"
echo "Executing command:"
echo $launch_cmd
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$client_hostname $launch_cmd 