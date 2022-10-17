#!/bin/bash
# https://stackoverflow.com/questions/525592/find-and-replace-inside-a-text-file-from-a-bash-command

replace_text_in_file () {
    local to_replace=$1
    local new_content=$2
    local filepath=$3
    while IFS='' read -r a; do
        echo "${a//$to_replace/$new_content}"
    done < $filepath > $filepath.t
    mv $filepath{.t,}
}

replace_text_in_file "$1" "$2" "$3"