import Data.Sequence (Seq)
data MolSeq = Sequence String String String

instance Show MolSeq where
    show (Sequence name seq variation) = name ++ " is a molecular sequence of type " ++ variation ++ ": " ++ seq

main :: IO ()
main = do
    let dnaMolecule = string2seq "TestDNA" "AAGTCCGTAGA"
    let proteinMolecule = string2seq "TestProtein" "AAGTCEGTAGA"
    let dnaMolecule2 = string2seq "TestDNA2" "AAGTCAGTAGA"
    let dnaMolecule3 = string2seq "TestDNA3" "GGAGGGAGGAG"
    print (show dnaMolecule)
    print (show proteinMolecule)
    print (seqName dnaMolecule)
    print (seqSequence dnaMolecule)
    print (seqLength dnaMolecule)
    print (calculateDifference dnaMolecule dnaMolecule2)
    print (seqDistance dnaMolecule dnaMolecule2)
    print (seqDistance dnaMolecule dnaMolecule3)
    print (typeHelper (seqSequence dnaMolecule))
    print (typeHelper (seqSequence dnaMolecule2))
    print (typeHelper (seqSequence dnaMolecule3))


string2seq :: String -> String -> MolSeq
string2seq name seq = Sequence name seq (typeHelper seq)

isDNA :: String -> Bool
isDNA = not . any (`notElem` "ACGT")

typeHelper :: String -> String
typeHelper dna = if isDNA dna
                then "DNA"
                else "Protein" 

seqName :: MolSeq -> String
seqName (Sequence name _ _) = name

seqSequence :: MolSeq -> String
seqSequence (Sequence _ seq _) = seq

seqLength :: MolSeq -> Int
seqLength (Sequence _ seq _) = length seq

seqDistance :: MolSeq -> MolSeq -> Double
seqDistance seq1 seq2 = distance
    where
        distance =
            let molType = typeHelper (seqSequence seq1)
                isDNA = molType == "DNA"
                {- Alpha = andelen positioner där sekvenserna skiljer sig åt -}
                alpha = calculateDifference seq1 seq2

            in  
                if molType /= typeHelper (seqSequence seq2)
                    then error "Invalid argument: Comparing DNA with Protein"
                    else let (const1, const2, const3) | isDNA = (3/4, 4, 3)
                                                      | otherwise = (19/20, 20, 19)

                             threshold = if isDNA then 0.74 else 0.4
                             result 
                                | alpha > threshold && isDNA = 3.3
                                | alpha > threshold = 3.7
                                | otherwise = -const1 * log (1 - (const2 * alpha / const3))
                         in result

calculateDifference :: MolSeq -> MolSeq -> Double
calculateDifference seq1 seq2 = calculateDifference' (seqSequence seq1) (seqSequence seq2) / fromIntegral (length (seqSequence seq1))
    where
        calculateDifference' :: String -> String -> Double
        calculateDifference' [] [] = 0
        calculateDifference' (x:xs) (y:ys) | x /= y = 1 + calculateDifference' xs ys
                                           | otherwise = calculateDifference' xs ys
