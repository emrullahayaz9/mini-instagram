# Mini Instagram API

Bu proje, temel bir sosyal medya uygulaması olan Mini Instagram API'sini sunar. Kullanıcılar post paylaşabilir, yorum yapabilir ve postları beğenebilir.
Postman collection link: https://emrullahayaz901-6731863.postman.co/workspace/emrullah-ayaz's-Workspace~6bbefea1-bb8b-42dc-8c01-1fdddd37abfa/collection/49737177-9a38188f-af7a-4663-80cc-c2b0aab6ef8f?action=share&creator=49737177
---

## Kurulum & Çalıştırma

1. Projeyi klonlayın:

```bash
git clone https://github.com/emrullahayaz9/mini-instagram.git
cd mini-instagram
```
# Gerekli bağımlılıkları yükleyin:
```bash
./mvnw clean install
```

# Uygulamayı başlatın:
```bash
./mvnw spring-boot:run
```
# Hazır ADMIN Kullanıcı

Projeyi ilk başlattığınızda aşağıdaki şekilde bir admin kullanıcı oluşturabilirsiniz:
```bash

POST /api/auth/signup
{
"username": "test_admin_user",
"password": "test",
"authKey": "test_admin_key"
}
```

authKey alanı admin yetkisi için zorunludur.

Normal kullanıcılar için bu alan boş bırakılabilir.

# API Uç Noktaları
## Auth
```bash

POST /api/auth/signup
Yeni kullanıcı kaydı (admin veya normal kullanıcı)
```

```bash
POST /api/auth/login
Kullanıcı giriş ve token alma
```
```bash

POST /api/auth/logout
Token iptal etme
```
```bash

GET /api/auth/me
Aktif kullanıcı bilgilerini döner (username, role)
```
## Users
```bash

GET /api/users/{id}
Kullanıcı detayları
```
```bash

PUT /api/users/me/password
Aktif kullanıcı şifre değişikliği
```
```bash

DELETE /api/users/me
Aktif kullanıcı hesabını siler
```
```bash

DELETE /api/admin/users/{id}
Admin tarafından herhangi bir kullanıcıyı silme
```
## Posts
```bash

POST /api/posts
Yeni post oluşturma (resim + açıklama)
```
```bash

GET /api/posts/{id}
Tekil post detayları
```
```bash

PUT /api/posts/{id}
Post güncelleme (yalnızca sahibi ve admin)
```
```bash

DELETE /api/posts/{id}
Post silme (yalnızca sahibi ve admin)
```
```bash

POST /api/posts/{id}/view
Görüntülenme sayısını artırma
```
```bash

GET /api/posts
Tüm aktif postları listeler
```
## Comments
```bash

POST /api/posts/{id}/comments
İlgili post için yorum ekleme
```
```bash

GET /api/posts/{id}/comments
İlgili postun yorumlarını listeleme
```
```bash

DELETE /api/comments/{id}
Yorum silme (yorum sahibi, post sahibi veya admin)
```
## Likes
```bash

POST /api/posts/{id}/likes
Postu beğenme (kullanıcı başına tek beğeni)
```
```bash

DELETE /api/posts/{id}/likes
Beğeniyi geri alma
```
# Varsayımlar & Kısıtlar

* Kullanıcı başına tek token geçerlidir; login olduğunda eski token yenilenir.

* Postlar sadece sahibi veya admin tarafından güncellenebilir veya silinebilir.

* Yorum silme yetkisi: yorum sahibi, post sahibi veya admin.

* Tüm API çağrıları için Authorization: Bearer <token> header gereklidir.

* Resim upload’ları sunucu tarafında app.upload-dir konumunda tutulur.

## Örnek Postman Akışı

Signup (Admin & Normal User)

Login → token al

Create Post → resim + açıklama

Get Posts / Get Post Details

Update / Delete Post (yetkili kullanıcı ile)

Add / Get / Delete Comments

Like / Unlike Post

Logout

Tüm endpoint’ler ve örnek istekler Postman Collection içinde mevcuttur
