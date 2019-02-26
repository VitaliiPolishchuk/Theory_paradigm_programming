count :: Eq a => a -> [a] -> Int
count x = length . filter (x==)

isOne :: Int-> [Int] -> Bool
isOne x xs = a /= 1
  where a = count x xs

myfilter :: (a -> [a] -> Bool) -> [a] -> [a] -> [a]
myfilter p [] _ = []
myfilter p (x:xs) l 
  | (p x l) = x:(myfilter p xs l)
  | otherwise = myfilter p xs l



main = do
  let l = [1, 1,2,3] 
  let res = myfilter isOne l l
  print res