VERSION=$(git describe --tags --abbrev=0)
docker build -t frugal-fennec-backend:"$VERSION" -t frugal-fennec-backend:latest ../
