
DATE_FOLDER=$(date +"%m-%d-%y-%H-%M")

cp -R build/reports/ $HOME/reports 
cp gradle/add-build-report.gradle $HOME
cp -R gradle/ $HOME/gradle
cp gradlew $HOME

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/jmcshane/search gh-pages > /dev/null

mkdir -p gh-pages/$DATE_FOLDER/reports/
cp -R reports gh-pages/$DATE_FOLDER/reports/
mv $HOME/gradlew gh-pages
mkdir -p gh-pages/gradle
mv -v $HOME/gradle/* gh-pages/gradle

cd gh-pages

./gradlew -b gradle/add-build-report.gradle addBuild -Pfolderdate=$DATE_FOLDER

rm -rf gradle/
rm -f gradlew

git add -f .
git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
