const fs = require('fs');
const path = require('path');

const src = path.join('..', '..', 'desktop', 'src');
const out = 'www';
const items = [
  'index.html',
  'privasi.html',
  'syarat.html',
  'manifest.webmanifest',
  'sw.js',
  'css',
  'icons',
  'js',
];

fs.rmSync(out, { recursive: true, force: true });
fs.mkdirSync(out);
for (const item of items) {
  fs.cpSync(path.join(src, item), path.join(out, item), { recursive: true });
}
console.log('Folder www siap.');