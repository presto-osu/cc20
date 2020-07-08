#!/bin/bash

MODULE=gator

JAR_FILE=./${MODULE}/build/libs/${MODULE}-1.0-SNAPSHOT-all.jar
APK_DIR=../apks

MAIN_CLASS=presto.Main

./gradlew :${MODULE}:shadowJar

APP=net.hubalek.android.apps.barometer
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.daily.bible.verse.app
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.agminstruments.drumpadmachine
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.paullipnyagov.hiphopdrumpads24
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.equibase.todaysracing
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.goforit.localtv
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.phonelocationtracker.track
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.mitula.homes
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=simon.sander.moonphases
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=il.talent.parking
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.SearingMedia.Parrot
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.reto.post.egydream
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.quick.world.news
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.speedlogicapp.speedlogiclite
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

APP=com.grupovidanta.android.app
echo "==================================: ${APP}"
java -cp ${JAR_FILE} ${MAIN_CLASS} ${APK_DIR}/${APP}.apk "${ANDROID_SDK}/platforms"

