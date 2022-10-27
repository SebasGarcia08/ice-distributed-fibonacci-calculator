export SSHPASS='swarch'

while getopts c:s:b:p:h flag
do 
        case "${flag}" in
                c) client_id=${OPTARG}
                    ;;
                s) server_ip_address=${OPTARG}
                    ;;
                b) branch=${OPTARG}
                    ;;
                p) client_callback_port=${OPTARG}
                    ;;
                h) echo "Usage: bash scripts/client/deploy.bash -s <server ip> -b <branch> -c <client id> -p <client callback port>"
                    echo ""
                    echo "For example:"
                    echo ""
                    echo "bash scripts/client/deploy.bash -s 10.147.19.124 -b feat/multithread -c 3 -p 9012"
                    echo ""
                    echo 'server ip: ip address of the server'
                    echo 'branch: branch to deploy, should be a valid branch from https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator repo ' 
                    echo "client id: the id of the client, e.g., if 2, then it refers to hgrid2"
                    echo "client callback port: the port in which to listen for callback communication"
                    exit 0
                    ;;
                *) echo "Invalid option -${flag}"
                    ;;
        esac
done


required_args=(client_id server_ip_address branch client_callback_port)
flag_names=(c s b p)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

client_hostname="xhgrid$client_id"

client_ip_address=$(getent hosts $client_hostname | awk '{ print $1 }')
client_callback_endpoints="default -h $client_ip_address -p $client_callback_port" 

repo_url="https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator.git"
repo_dir="ice-distributed-fibonacci-calculator"

cmd1="cd $repo_dir"
cmd2="java -jar client/build/libs/client.jar < input.txt"
launch_cmd="${cmd1} && ${cmd2}"

echo "Server ip address: '$server_ip_address'"
echo "Client hostname: '$client_hostname' ip address: '$client_ip_address'"
echo "Repo url: '$repo_url', clone dir: '$repo_dir' branch: '$branch'"
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$client_hostname "rm -rf $repo_dir; git clone --branch $branch $repo_url "

echo "------------------------------------"
to_replace_arr=("Ice.Default.Host=10.147.19.124" "Callback.Endpoints = default -h 10.147.19.124  -p 9777")
new_content_arr=("Ice.Default.Host=${server_ip_address}" "Callback.Endpoints = ${client_callback_endpoints}")
file_to_replace="client/src/main/resources/config.client"
echo "Replacing ip adresses in $file_to_replace file..."

for i in "${!to_replace_arr[@]}"; do
        echo "Replacing '${to_replace_arr[$i]}' with '${new_content_arr[$i]}'"
        base_cmd="cd $repo_dir && bash scripts/utils/replace_text_in_file.bash"
        cmd_to_adjust_config="$base_cmd '"${to_replace_arr[$i]}"' '"${new_content_arr[$i]}"' "$file_to_replace""
        echo "Running command: $cmd_to_adjust_config"
        sshpass -e ssh -o StrictHostKeyChecking=no \
            swarch@$client_hostname "$cmd_to_adjust_config"
done

echo "Done."
echo "Now config file looks like this"
cat $file_to_replace
echo "------------------------------------"

echo 'Compiling...'
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$client_hostname "cd $repo_dir && ./gradlew build"

echo "Running client with ip address: $client_ip_address (callback on port $client_callback_port)"
echo "Executing command:"
echo $launch_cmd
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$client_hostname $launch_cmd &