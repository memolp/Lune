local cur = os.time()
local i =0
local total = 0
while(i< 100000000) do
    total = total + i
    i = i + 1
end
local erd = os.time()
print("cost:....", erd - cur)