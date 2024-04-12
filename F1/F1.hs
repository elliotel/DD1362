import Data.Char ( isAlpha )

fib :: Integer -> Integer
fib n = fib' 0 1 n
    where
        fib' :: Integer -> Integer -> Integer -> Integer
        fib' prev _ 0 = prev
        fib' prev curr n = fib' curr (prev + curr) (n-1)

rovarsprak :: String -> String
rovarsprak string = rovarsprak' string ""
    where
        rovarsprak' :: String -> String -> String
        rovarsprak' "" res = reverse res
        rovarsprak' (x:xs) res
            | x `elem` "bcdfghjklmnpqrstvwxz" =  rovarsprak' xs (x : 'o' : x : res)
            | otherwise = rovarsprak' xs (x: res)


karpsravor :: String -> String
karpsravor "" = ""
karpsravor (x:xs)
    | x `elem` "bcdfghjklmnpqrstvwxz" =  x : karpsravor (drop 2 xs)
    | otherwise = x : karpsravor xs

medellangd :: String -> Double
medellangd "" = 0
medellangd str = medellangd' (splitWords str) 0 0
    where
        splitWords :: String -> [String]
        splitWords "" = []
        splitWords (x:xs)
            | isAlpha x = splitWords' (x:xs) "" []
            | otherwise = splitWords xs
                where
                    splitWords' :: String -> String -> [String] ->[String]
                    splitWords' [] curr fin
                        | null curr = fin
                        | otherwise = curr : fin
                    splitWords' (y:ys) curr fin
                        | isAlpha y = splitWords' ys (curr ++ [y]) fin
                        | null curr = splitWords' ys "" fin
                        | otherwise = splitWords' ys "" (curr : fin)
        medellangd' :: [String] -> Double -> Double -> Double
        medellangd' [] sum count = sum / count
        medellangd' (x:xs) sum count = medellangd' xs (sum + fromIntegral (length x)) (count + 1)

skyffla :: [a] -> [a]
skyffla [] = []
skyffla numbers = skyffla' numbers []
    where
        skyffla' :: [a] -> [[a]]-> [a]
        skyffla' rem splits
            | null rem = concat splits
            | otherwise = let   
                listTuple = split rem [] [] 0
                in
                skyffla' (snd listTuple) (splits ++ [fst listTuple])
        split :: [a] -> [a] -> [a] -> Int -> ([a], [a])
        split original split1 split2 i
            |   null original = (split1, split2)
            |   otherwise = let nextTuple = split (tail original) split1 split2 (i+1)
                        in if even i
                            then (head original : fst nextTuple, snd nextTuple)
                            else (fst nextTuple, head original : snd nextTuple)



