#!/bin/bash
## File : docker-healthcheck.sh
## Author : developer@coinsthai.com
## Description :
## --
## Created : <2017-03-28>
## Updated: Time-stamp: <2017-06-19 19:28:47>
##-------------------------------------------------------------------
set -e
# Quit, if mandatory envs are not set
curl http://localhost:80/ | grep 'is up'
## File : docker-healthcheck.sh ends
