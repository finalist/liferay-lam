#!/usr/bin/env bash


#if git diff-index --quiet HEAD --; then
if true; then
    echo Enter the version to release:
    read VERSION

    echo Setting Bundle-Version to $VERSION in bnd.bnd files
    find . -name bnd.bnd -exec perl -p -i -e "s/Bundle-Version: .*/Bundle-Version: $VERSION/g" {} \;

    echo ./gradlew build
    GRADLE_RESULT=$?

    if ./gradlew build ; then

        echo "Uploading artifacts to Nexus..."
        ./gradlew uploadArchives
        echo "Upload done"
        git commit -am "Release $VERSION"
        git tag $VERSION
        echo "Committed and tagged version $VERSION, push manually please."
    else
        echo Gradle build failed. Fix and re-run release script.
    fi
else
    echo Local changes detected, aborting.
fi


