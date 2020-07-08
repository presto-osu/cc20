#!/bin/bash

ep=$1
tag=$2 # mean_l1, mean_hotcov
c=$3
post=postprocessing #qp

echo ===============
echo --------------- -1
cat log/rr.binomial.eln$ep.gs-1.$post.stats | grep $tag | cut -f $c
echo --------------- 100
cat log/rr.binomial.eln$ep.gs100.$post.stats | grep $tag | cut -f $c
echo --------------- 10
cat log/rr.binomial.eln$ep.gs10.$post.stats | grep $tag | cut -f $c
echo --------------- 1
cat log/rr.binomial.eln$ep.gs1.$post.stats | grep $tag | cut -f $c

