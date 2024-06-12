echo "Building app..."
npm run build
echo "Deploy files to server..."
scp -r build/* root@167.71.200.75:/var/www/html
echo "Done!"