
DATE_FOLDER=$(date +"%m-%d-%y-%H-%M")

cp -R build/reports/ $HOME/reports 

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/jmcshane/search gh-pages > /dev/null

mkdir -p gh-pages/$DATE_FOLDER/benchmark

cp -R reports gh-pages/$DATE_FOLDER/

cd gh-pages

git add -f .
git commit -m "Latest javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git remote -v
git status
git push -fq origin gh-pages > /dev/null
