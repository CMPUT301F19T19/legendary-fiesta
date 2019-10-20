#!/bin/bash -e
git config --global user.email 'hubot@users.noreply.github.com'
git config --global user.name 'Azure CI'

GIT_REV="$(git rev-parse --short HEAD)"
./gradlew javadoc 2> /dev/null
[ -z "${BUILD_SOURCESDIRECTORY}" ] && BUILD_SOURCESDIRECTORY="$(pwd)"
pushd /tmp/
git clone https://github.com/CMPUT301F19T19/CMPUT301F19T19.github.io pages
cd -- pages
rm -rf javadoc
cp -ar "${BUILD_SOURCESDIRECTORY}/app/build/javadoc" .
git add javadoc
git commit -m "Automatically generated javadoc for ${GIT_REV}"
git push https://${PUSH_CRED}@github.com/CMPUT301F19T19/CMPUT301F19T19.github.io.git
popd
