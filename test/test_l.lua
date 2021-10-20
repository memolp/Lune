local test_func = function(param1)
    return param1 - 1
end

local cur = os.time()
local i =0
local total = 0
while(i< 100000000) do
    total = total + test_func(i)
    i = i + 1
end
local erd = os.time()
print("cost:....", erd - cur)