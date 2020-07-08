#!/bin/bash

MODULE=randomized-response-binomial-projection

JAR_FILE=./${MODULE}/build/libs/${MODULE}-1.0-SNAPSHOT-all.jar
DATASET_DIR=../dataset
EPSILON=$1
GS=$2
TRIALS=100
CSVDIR=$3

MAIN_CLASS=presto.MainRRBinomialProjection

./gradlew :${MODULE}:shadowJar

APP=barometer
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=bible
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=dpm
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=drumpads
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=equibase
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=localtv
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=loctracker
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=mitula
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=moonphases
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=parking
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/${APP} ${DATASET_DIR}/${APP}.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=parrot
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/parrot ${DATASET_DIR}/parrot.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=post
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/post ${DATASET_DIR}/post.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=quicknews
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/quicknews ${DATASET_DIR}/quicknews.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=speedlogic
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/speedlogic ${DATASET_DIR}/speedlogic.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"

APP=vidanta
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${DATASET_DIR}/vidanta ${DATASET_DIR}/vidanta.json "${EPSILON}" ${TRIALS} "${GS}" "${CSVDIR}"
