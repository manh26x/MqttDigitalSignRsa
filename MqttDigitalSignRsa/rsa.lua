local rsaFunctions = {}

math.randomseed(os.time())
math.random() math.random() math.random()

--decompose n-1 as (2^s)*d
local function decompose(negOne)
    local exponent, remainder = 0, negOne
    while (remainder%2) == 0 do
        exponent = exponent+1
        remainder = remainder/2
    end
    assert((2^exponent)*remainder == negOne and ((remainder%2) == 1), "Error setting up s and d value")
    return exponent, remainder
end

local function isNotWitness(n, possibleWitness, exponent, remainder)
    local witness = fastModExp(possibleWitness, remainder, n)

    if (witness == 1) or (witness == n-1) then
        return false
    end

    for _=0, exponent-1 do
        witness = fastModExp(witness, 2, n)
        if witness == (n-1) then
            return false
        end
    end

    return true
end

--using miller-rabin primality testing
--n the integer to be tested, k the accuracy of the test
function isProbablyPrime(n, accuracy)
    if n <= 3 then
        return n == 2 or n == 3
    end
    if (n%2) == 0 then
        return false
    end

    local exponent, remainder, witness = decompose(n-1)

    --checks if it is composite
    for i=0, accuracy do
        witness = math.random(2, n - 2)
        if isNotWitness(n, witness, exponent, remainder) then
            return false
        end
    end

    --probably prime
    return true
end

function frexp(x)
    if x == 0 then return 0.0,0.0 end
    local e = math.floor(math.log(math.abs(x)) / math.log(2, 10))
    if e > 0 then
        -- Why not x / 2^e? Because for large-but-still-legal values of e this
        -- ends up rounding to inf and the wheels come off.
        x = x * 2^-e
    else
        x = x / 2^e
    end
    -- Normalize to the range [0.5,1)
    if math.abs(x) >= 1.0 then
        x,e = x/2,e+1
    end
    return x,e
end

function toBits(num)
    local t={}
    --math.frexp(n) -> m, e  :  n = m2^e; 0.5 <= m < 1
    --so this sets i to the total amount of binary digits w/o padding
    for i=select(2,frexp(num)),1,-1 do
        rest=math.fmod(num,2)
        t[i]=rest
        num=math.floor((num-rest)/2)
    end
    return t
end

--left to right
function fastModExp(num, expBits, mod)
    if type(expBits) == "number" then
        expBits = toBits(expBits)
    end

    local result = 1
    for i=1, #expBits do
        result=(result*result)%mod
        if expBits[i]==1 then
            result=(result*num)%mod
        end
    end
    return result
end

--best practice: a>b
function gcd(a, b)
    while a%b ~= 0 do
        a, b = b, a%b
    end
    return b
end

--best practice: b>a
function egcd(a, b)
    local x,y, u,v, q,r, m,n = 0,1, 1,0
    while a~=0 do
        q,r = math.floor(b/a), b%a
        m,n = x-(u*q), y-(v*q)
        b,a, x,y, u,v = a,r, u,v, m,n
    end
    return b, x, y
end

function encrypt(params)
    return fastModExp(params[1], params[2], params[3])
end

function encrypt_byte(params)
    local e = params[2]
    local n = params[3]
    return fastModExp(params[1], e, n)
end
function decrypt(crypt, d, n)
    return fastModExp(crypt, d, n)
end
--print(generateKeys(16))
--encryptedmsg  = encrypt({22.5, 5, 20453369})
function check_hash(params)
    local value = params[1]
    local salt =parmas[2]
    local hash = params[3]
    assert(hash == (value * salt + 123 + math.floor(value * (11/(salt+3)) + 321)) % math.floor(salt^0.77), 'invalid value')

end
