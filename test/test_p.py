import time

def test_fun(param1):
    return param1 - 1 
cur = time.time()
i = 0
total = 0
while i < 100000000:
    total = total + test_fun(i)
    i = i + 1

erd = time.time()
print("cost....", erd - cur)