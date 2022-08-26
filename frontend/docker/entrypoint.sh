#!/bin/sh

set -x

env_properties=""
for line in $(env | grep REACT); do
  # Split env variables by character `=`
  if printf '%s\n' "$line" | grep -q -e '='; then
    varname=$(printf '%s\n' "$line" | sed -e 's/=.*//')
    varvalue=$(printf '%s\n' "$line" | sed -e 's/^[^=]*=//')
  fi
  
  env_properties="$env_properties $varname: \"$varvalue\","
done

sed -i "s|INSERT_ENV_HERE:\"\"|$env_properties|g" /app/index.html

exec "$@"
