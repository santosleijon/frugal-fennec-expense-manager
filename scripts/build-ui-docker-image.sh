VERSION=$(git describe --tags --abbrev=0)

cd ../ui
npm run build

docker build -t frugal-fennec-ui:"$VERSION" -t frugal-fennec-ui:latest ./
