
DATE_FOLDER=$(date +"%m-%d-%y-%H-%M")
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/jmcshane/search gh-pages > /dev/null

cd gh-pages
mkdir -p $DATE_FOLDER/benchmark

cd ..

cp -R build/reports/ gh-pages/$DATE_FOLDER/

git add -f .
git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
