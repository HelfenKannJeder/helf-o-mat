#!/bin/bash
ansible-playbook -i inventories/production/hosts.yml local.yml --vault-id @prompt