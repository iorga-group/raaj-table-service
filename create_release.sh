#!/bin/bash
set -x

TAG=$1

TMP_DIR=/tmp/iraj-release

# Create trap function which will stops the program if there is a problem with sub-calls
trap catch_error ERR;
function catch_error {
	echo "Problem occured, stopping. (last return code : $?)"
	exit 2
}

trap catch_int INT;
function catch_int {
	echo "Stopped with INT signal."
	exit 3
}

rm -rf $TMP_DIR
git clone ./ $TMP_DIR -b master

cd $TMP_DIR

if [ -n "$TAG" ]
then
	git checkout $TAG
else
	TAG=`git describe --tags`
fi

#sed -i "s/\\\$Id:.*\\\$/\$Id: `git describe --tags` \$/" isi/pom.xml seam-blank-parent/pom.xml
sed -i "s/\\\$Id.*\\\$/`git describe --tags`/" iraj/pom.xml iraj-bom/pom.xml irajblank-parent/pom.xml

# Suppression des références "SNAPSHOT"
find . -name "pom.xml" -exec sed -i "s/-SNAPSHOT//g" {} \;

NEW_RELEASE_DIR_NAME=iraj-$TAG
NEW_RELEASE_DIR=/tmp/$NEW_RELEASE_DIR_NAME

rm -rf $NEW_RELEASE_DIR
mkdir -p $NEW_RELEASE_DIR/docs
mkdir -p $NEW_RELEASE_DIR/src

cp -R * .git* $NEW_RELEASE_DIR/src/

cd iraj-bom
mvn site
cd ..

for PROJECT in iraj iraj-bom
do
	cp -R $PROJECT/target/site $NEW_RELEASE_DIR/docs/$PROJECT
done

cd $NEW_RELEASE_DIR

7z a $NEW_RELEASE_DIR.7z * .git*

echo "End of script"
read PAUSE
