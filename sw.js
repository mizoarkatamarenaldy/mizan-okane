const CACHE_NAME = 'mizan-v3';
const ASSETS_TO_CACHE = [
  './',
  './index.html',
  './css/style.css',
  './js/app.js',
  './js/db.js',
  './js/sync.js',
  './manifest.webmanifest',
  './icons/icon-192x192.png',
  './icons/icon-512x512.png',
  './icons/icon-192x192-maskable.png',
  './icons/icon-512x512-maskable.png'
];

// Request ke layanan Google (login dan Drive API) tidak boleh di-cache:
// isinya token dan data pribadi, dan harus selalu langsung ke jaringan.
const NEVER_CACHE_HOSTS = ['googleapis.com', 'accounts.google.com', 'gstatic.com'];

function isNeverCacheHost(hostname) {
  return NEVER_CACHE_HOSTS.some((host) => hostname === host || hostname.endsWith(`.${host}`));
}

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(ASSETS_TO_CACHE);
    })
  );
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cache) => {
          if (cache !== CACHE_NAME) {
            return caches.delete(cache);
          }
        })
      );
    })
  );
  self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  const url = new URL(event.request.url);

  // Tanpa respondWith, browser menangani request seperti biasa (tanpa cache SW).
  // Hanya aset GET dari origin app sendiri yang dilayani dari cache.
  if (event.request.method !== 'GET') return;
  if (isNeverCacheHost(url.hostname)) return;
  if (url.origin !== self.location.origin) return;

  event.respondWith(
    caches.match(event.request).then((response) => {
      return response || fetch(event.request);
    })
  );
});
