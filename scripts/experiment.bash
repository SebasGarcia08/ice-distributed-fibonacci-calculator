echo 'Enter the number of clients to run the experiment with' 
read num_clients

echo 'Enter the maximum i-th fibonacci number to calculate'
read max_fib

echo 'Enter the maximum wait time among requests'
read max_wait

echo 'Enter the maximum java heap size (default 512m)'
read max_heap

echo "Compressing files..."
zip -r helloworld-ciclo-kbd-AlejandraDiaz-SebastianGarcia.zip .
echo 'Done.'

echo "Installing sshpass..."
sudo apt-get install sshpass
echo 'Done.'

