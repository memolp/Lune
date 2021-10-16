import time

cur = time.time()
i = 0
total = 0
while i < 100000000:
    total = total + i
    i = i + 1

erd = time.time()
print("cost....", erd - cur)