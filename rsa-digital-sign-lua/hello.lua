local resty_rsa = require "resty.rsa"
--生成公钥和私钥
--[[
local rsa_public_key1, rsa_priv_key1, err = resty_rsa:generate_rsa_keys(512)
if not rsa_public_key1 then
    ngx.say('generate rsa keys err: ', err)
end

ngx.say(rsa_public_key1)

ngx.say(rsa_priv_key1)
]]
--私钥
local rsa_priv_key = [[-----BEGIN RSA PRIVATE KEY-----
MIIBOwIBAAJBAKjMyC+BImsChQlNXeBMTjXDIQbzVFEzc0q2GUUGs5fL/VIO9Bwv
YDUQr/5ocKx3l86qN2/jHtRmGjLw5nkakdECAwEAAQJBAIZEBUOMAvV9Vpa0nGRK
Lbej00R1Dm9cbmtR9z2pe/bT87jyvprMQlS1y3gkB70McvVMneoYf1YQv9oIr98k
m7UCIQDyajM7ps1PaDpPHmRYWjGnJN9Yt3ElZu9nLcJNEzLhwwIhALJCd4aYdlZQ
YooT6XBzr54aP8XVX45tH9h7SpJ299DbAiEA006dgCbjGo/JHARrBdUBKShsA+JL
n4W9s5vgndzZYo8CIHyAedTS9YvRdxFzWM7Grfjh4nq9TZE/XEepzOrBFtKTAiAn
DzJu8xpGMYoYLIhtXTu2ag7stY5Ni2FnNZeOlbtM7A==
-----END RSA PRIVATE KEY-----]]
--公钥
local rsa_public_key = [[-----BEGIN PUBLIC KEY-----
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKjMyC+BImsChQlNXeBMTjXDIQbzVFEz
c0q2GUUGs5fL/VIO9BwvYDUQr/5ocKx3l86qN2/jHtRmGjLw5nkakdECAwEAAQ==
-----END PUBLIC KEY-----]]

--[[ 加密
local pub, err = resty_rsa:new({ public_key = rsa_public_key })
if not pub then
    ngx.say("new rsa err: ", err)
    return
end
local encrypted, err = pub:encrypt("测试字符串")
if not encrypted then
    ngx.say("failed to encrypt: ", err)
    return
end
ngx.say("encrypted length: ", ngx.encode_base64(encrypted))
]]

--解密
local encrypted = "Nx2IW62S4ZCjn46CjL00HQcckFTNWVqs2jxQRnw+M1AMihZbagBjyx2249Kqzz6wpMO8/PL2qogWsILzLr/wHQ=="
--解密
local priv, err = resty_rsa:new({ private_key = rsa_priv_key })
if not priv then
    ngx.say("new rsa err: ", err)
    return
end
local decrypted = priv:decrypt(ngx.decode_base64(encrypted))
ngx.say(decrypted)