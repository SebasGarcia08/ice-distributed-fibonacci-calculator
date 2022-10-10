export SSHPASS='swarch'

if [ -z $1 ]; then
    echo 'Missing required argument hostname to deploy (e.g. xhgrid1)'
    exit 1
fi

hostname=$1

echo "Deploying to $hostname..."
echo "Copying files..."
sshpass -e ssh swarch@$hostname \
        'rm -rf ice-distributed-fibonacci-calculator'
sshpass -e scp -r -o StrictHostKeyChecking=no \
    . swarch@$hostname:~/sebas-aleja
echo 'Done.'