const path = require('path');
const fs = require('fs')

const sourceFile = path.join(__dirname, "node", "node_modules", "@paclife", "plds.tokens", "figmagic");

fs.readdir(sourceFile, function(err, files) {
    console.log(files);
    if (err) {
        return console.log('Unable to scan directory: ' + err);
    }
    files.forEach(function(file) {
        if (file === 'index.js') return
        if (file.includes('.js')) {
            const module = require(`${sourceFile}/${file}`)
            let lessFileData = ''

            Object.keys(module).forEach((el) => {
                lessFileData = lessFileData + "\n" + "@" + el + " : " + module[el] + ";"
            })

            file = file.replace('.js', '')
            const destinationPath = path.join(__dirname, "..", "ui.apps", "src", "main", "content", "jcr_root", "apps", "plweb-dsys", "clientlibs", "clientlib-site", "css", "design-tokens");
            console.log(destinationPath);
            fs.writeFile(`${destinationPath}/${file}.less`, lessFileData, (err) => {
                if (err)
                    console.log(err);
                else {
                    console.log("File written successfully\n");
                    console.log("The written has the following contents:");
                    console.log(fs.readFileSync(`${destinationPath}/${file}.less`, "utf8"));
                }
            });
        }
    });
});