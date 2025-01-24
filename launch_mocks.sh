#!/bin/bash
set -x
export WINDOWS_GATEWAY_IP=$(cat /etc/resolv.conf | grep -m 1 nameserver | sed -e 's/nameserver[[:space:]]\+//g');
printf "Windows gateway ip address detected: %s\n" "$WINDOWS_GATEWAY_IP";
export WIREMOCK_FORWARD_PORT=8181;

export CUR_WORKDIR="subst_template_out";
mkdir $CUR_WORKDIR;

export WIREMOCK_TEMPLATE_DIR=./wiremock_templates;
export WIREMOCK_TARGET_DIR=/mnt/c/users/plemire/code/autobook-backend/wiremock/mappings;

# make the substitutions in the templates
## TODO a foreach !
envsubst < ./wiremock_templates/vehiclesGetStubProxy.json > "$CUR_WORKDIR/wiremock_vehiclesGetStubProxy.json"


# delete the old template targets at the template terget root dir
rm -rf "$WIREMOCK_TARGET_DIR"/subs_.*;

# move in the substituted active targets
## TODO a foreach !
mv "$CUR_WORKDIR/wiremock_vehiclesGetStubProxy.json" "$WIREMOCK_TARGET_DIR/subs_vehiclesGetStubProxy.json"


# delete this task work dir
rm -rf "$CUR_WORKDIR";

# start|restart the impacted docker processes
