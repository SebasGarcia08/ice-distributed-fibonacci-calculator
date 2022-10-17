## Server

In order to run the server:

```bash
❯ bash scripts/server/deploy.bash -h
Usage: bash scripts/server/deploy.bash -s <server_ip> -b <branch>

For example:

bash scripts/server/deploy.bash -s 22 -b feat/multithread

server id: the id of the server, e.g., if 2, then it refers to hgrid2"
branch: branch to deploy, should be a valid branch from https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator repo
```

```bash
❯ bash scripts/server/deploy.bash -s 22 -b feat/multithread
```

## Client

```bash
❯ bash scripts/client/deploy.bash -h
Usage: bash scripts/client/deploy.bash -s <server ip> -b <branch> -c <client id> -p <client callback port>

For example:

bash scripts/client/deploy.bash -s 10.147.19.124 -b feat/multithread -c 3 -p 9012

server ip: ip address of the server
branch: branch to deploy, should be a valid branch from https://github.com/SebasGarcia08/ice-distributed-fibonacci-calculator repo 
client id: the id of the client, e.g., if 2, then it refers to hgrid2
client callback port: the port in which to listen for callback communication
```

```bash
bash scripts/client/deploy.bash -s 10.147.19.124 -b feat/multithread -c 3 -p 9012
```