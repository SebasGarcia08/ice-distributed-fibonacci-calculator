echo "Compressing files..."
zip -r sebas-aleja.zip .

echo "Installing sshpass..."
sudo apt-get install sshpass

echo "Enter the hostnames you want to copy this project to"
IFS=, read -ra names

for hostname in "${names[@]}"; do
    echo "Copying to $hostname"
    sshpass -p "swarch" scp sebas-aleja.zipexi swarch@$hostname:~/
    echo "Done."
done

for hostname in "${names[@]}"; do
    echo "Compiling on $hostname"
    sshpass -p "swarch" ssh swarch@$hostname "unzip sebas-aleja.zip -d sebas-aleja; rm sebas-aleja.zip; cd ./sebas-aleja && ./gradlew build" &
    echo "Done."
done