
DATE_FOLDER=$(date +"%m-%d-%y-%H-%M")

cp -R build/reports/ $HOME/reports 
cp gradle/add-build-report.gradle $HOME
cp -R gradle/ $HOME/gradle
cp gradlew $HOME
if [ -f boot/build/benchmark/search-benchmark.html ]; then
	cp boot/build/benchmark/search-benchmark.html $HOME
fi

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/jmcshane/search gh-pages > /dev/null

mkdir -p gh-pages/$DATE_FOLDER/benchmark

cp -R reports gh-pages/$DATE_FOLDER/
if [ -f boot/build/benchmark/search-benchmark.html ]; then
	cp $HOME/search-benchmark.html gh-pages/$DATE_FOLDER/benchmark
fi
mv $HOME/gradlew gh-pages
mv -R $HOME/gradle gh-pages

cd gh-pages

./gradlew -b gradle/add-build-report.gradle addBuild -Pfolderdate=$DATE_FOLDER

rm -rf gradle/
rm -f gradlew

git add -f .
git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git remote -v
git status
git push -fq origin gh-pages > /dev/null
