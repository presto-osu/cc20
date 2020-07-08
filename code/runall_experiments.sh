#!/bin/bash

run() {
  v=$1
  echo $v
  ep=$(python -c "import math; print(math.log($v))")

  for gs in -1 1 10 100
  do
    echo "$ep $gs ../csv-rr-eln$v"
    bash runall_binomial_projection.sh $ep $gs ../csv-rr-eln$v #| tee log/rr.binomial.eln9.gs${gs}.proj
    bash runall_stats.sh $ep $gs postprocessing ../csv-rr-eln$v | tee log/rr.binomial.eln$v.gs${gs}.postprocessing.stats.log
    bash runall_stats.sh $ep $gs qp ../csv-rr-eln$v | tee log/rr.binomial.eln$v.gs${gs}.qp.stats.log
  done
}


for v in 9 25 49 81 121
do
  run $v
done
