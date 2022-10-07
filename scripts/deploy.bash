echo "Compressing files..."
zip -r helloworld-ciclo-kbd-AlejandraDiaz-SebastianGarcia.zip .

echo "Installing sshpass..."
sudo apt-get install sshpass

echo "Enter the hostnames you want to copy this project to"
IFS=, read -ra names

for hostname in "${names[@]}"; do
    echo "Copying to $hostname"
    sshpass -p "swarch" scp helloworld-ciclo-kbd-AlejandraDiaz-SebastianGarcia.zip swarch@$hostname:~/
    echo "Done."
done

for hostname in "${names[@]}"; do
    echo "Compiling on $hostname"
    sshpass -p "swarch" ssh swarch@$hostname "unzip helloworld-ciclo-kbd-AlejandraDiaz-SebastianGarcia.zip -d sebas-aleja; rm sebas-aleja.zip; cd ./sebas-aleja && ./gradlew build" &
    echo "Done."
do