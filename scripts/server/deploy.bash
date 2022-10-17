export SSHPASS='swarch'

while getopts 's':'b':'h' flag
do 
        case "${flag}" in
                s) server_id=${OPTARG}
                    ;;
                b) branch=${OPTARG}
                    ;;
                h) echo "Usage: bash scripts/server/deploy.bash -s <server_ip> -b <branch>"
                    echo ""
                    echo "For example:"
                    echo ""
                    echo "bash scripts/server/deploy.bash -s 22 -b feat/multithread"
                    echo ""
                    echo 'server id: the id of the server, e.g., if 2, then it refers to hgrid2"'
                    echo 'branch: branch to deploy, should be a valid branch from https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator repo ' 
                    exit 0
                    ;;
                *) echo "Invalid option -${flag}"
                    ;;
        esac
done

required_args=(server_id branch)
flag_names=(s b)

for i in "${!required_args[@]}"; do
        if [ -z "${!required_args[$i]}" ]; then
                echo "Missing required argument -${flag_names[$i]}"
                exit 1
        fi
done

server_hostname="xhgrid$server_id"
server_ip_address=$(getent hosts $server_hostname | awk '{ print $1 }')
repo_url="https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator.git"
repo_dir="ice-distributed-fibonacci-calculator"

echo "Server hostname: '$server_hostname' ip address: '$server_ip_address'"
echo "Repo url: '$repo_url', clone dir: '$repo_dir' branch: '$branch'"
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$server_hostname "rm -rf $repo_dir; git clone --branch $branch $repo_url "

echo 'Compiling...'
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$server_hostname "cd $repo_dir && ./gradlew build"

echo "Running server on ip address: $server_ip_address"
sshpass -e ssh -o StrictHostKeyChecking=no \
    swarch@$server_hostname "cd $repo_dir && killall java && java -jar server/build/libs/server.jar --Ice.Default.Host=$server_ip_address"