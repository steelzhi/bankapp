set -x;
sh gradle clean;
./gradle-bin clean;
sh gradlew build --exclude-task test --no-build-cache --no-configuration-cache;

