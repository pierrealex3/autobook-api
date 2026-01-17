#!/bin/bash
set -x

set -a
source .env
set +a

export WINDOWS_GATEWAY_IP=$(cat /etc/resolv.conf | grep -m 1 nameserver | sed -e 's/nameserver[[:space:]]\+//g');
printf "Windows gateway ip address detected: %s\n" "$WINDOWS_GATEWAY_IP";
export WIREMOCK_FORWARD_PORT=8181;

export CUR_WORKDIR=./subst_template_out;
export MAPPINGS_WORKDIR=$CUR_WORKDIR/mappings;
export FILES_WORKDIR=$CUR_WORKDIR/__files;
mkdir -p $MAPPINGS_WORKDIR $FILES_WORKDIR;

export WIREMOCK_TEMPLATE_MAPPINGS_DIR=./wiremock_templates/mappings;
export WIREMOCK_TEMPLATE_FILES_DIR=./wiremock_templates/__files;
export WIREMOCK_TARGET_WIREMOCK_TEMPLATE_MAPPINGS_DIR="$WIREMOCK_HOME"/mappings;
export WIREMOCK_TARGET_WIREMOCK_TEMPLATE_FILES_DIR="$WIREMOCK_HOME"/__files;

# make the substitutions in the mapping templates
for file in "$WIREMOCK_TEMPLATE_MAPPINGS_DIR"/subs_*.json; do
  printf "substitute env vars in file: %s\n" "$file"
  printf "target destination is: %s\n" "$MAPPINGS_WORKDIR/${file##*/}"
  envsubst < "$file" > "$MAPPINGS_WORKDIR/${file##*/}"
done

# make the substitutions in the files templates
for file in "$WIREMOCK_TEMPLATE_FILES_DIR"/subs_*.json; do
  printf "substitute env vars in file: %s\n" "$file"
  printf "target destination is: %s\n" "$FILES_WORKDIR/${file##*/}"
  envsubst < "$file" > "$FILES_WORKDIR/${file##*/}"
done

# delete the old template targets at the template terget root dir
rm -rf "$WIREMOCK_TARGET_WIREMOCK_TEMPLATE_MAPPINGS_DIR"/subs_.*;
rm -rf "$WIREMOCK_TARGET_WIREMOCK_TEMPLATE_FILES_DIR"/subs_.*;

# move in the substituted active targets
for file in "$MAPPINGS_WORKDIR"/*; do
  printf "promote to wiremock target directory this treated file: %s\n" "$file"
  mv "$file" "$WIREMOCK_TARGET_WIREMOCK_TEMPLATE_MAPPINGS_DIR/${file##*/}"
done

for file in "$FILES_WORKDIR"/*; do
  printf "promote to wiremock target directory this treated file: %s\n" "$file"
  mv "$file" "$WIREMOCK_TARGET_WIREMOCK_TEMPLATE_FILES_DIR/${file##*/}"
done

# delete this task work dir
rm -rf "$CUR_WORKDIR";

# start|restart the impacted docker processes
