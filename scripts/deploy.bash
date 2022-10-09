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

echo "Unzipping files..."
sshpass -e ssh -o StrictHostKeyChecking=no \
        swarch@$hostname \
        unzip -o sebas-aleja.zip -d sebas-aleja &&\
        rm -rf sebas-aleja.zip && \
        cd sebas-aleja 
        
echo 'Done.'