#!/bin/bash
for i in $(seq 1 31); do
    echo '\n' >> known_hosts
    hostname="xhgrid$i"
    ssh-keygen -R $hostname
    ssh-keyscan $hostname >> ~/.ssh/known_hosts
done