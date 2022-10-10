export SSHPASS='swarch'

if [ -z $1 ]; then
    echo 'Missing required argument hostname to deploy (e.g. xhgrid1)'
    exit 1
fi

hostname=$1

rm -rf sebas-aleja.zip
zip -r sebas-aleja.zip .

echo "Deploying to $hostname..."
echo "Copying files..."
sshpass -e scp -v -o StrictHostKeyChecking=no \
    sebas-aleja.zip swarch@$hostname:~/ 
echo 'Done.'

server_ip_address=$(getent hosts xhgrid21 | awk '{ print $1 }')

echo "$hostname IP address: $server_ip_address"

echo "Unzipping files..."
sshpass -e ssh -o StrictHostKeyChecking=no \
        swarch@$hostname \
        unzip -o sebas-aleja.zip -d sebas-aleja &&\
        rm -rf sebas-aleja.zip && \
        java -jar server/build/libs/server.jar --Ice.Default.Host=$server_ip_address &
        
echo 'Done.'