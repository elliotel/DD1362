import Data.List

class Evol a where
    distance :: a -> a -> Double
    name :: a -> String
    distanceMatrix :: [a] -> [(String, String, Double)]
    distanceMatrix list = [(name a, name b, distance a b) | a <- list, b <- list, name a <= name b]


data MolSeq = Sequence String String String

instance Evol MolSeq where
    distance :: MolSeq -> MolSeq -> Double
    distance = seqDistance
    name :: MolSeq -> String
    name = seqName

data Profile = Prof [[(Char, Int)]] String Int String

instance Evol Profile where
    distance :: Profile -> Profile -> Double
    distance = profileDistance
    name :: Profile -> String
    name = profileName


instance Show MolSeq where
    show :: MolSeq -> String
    show (Sequence name seq variation) = "Molecular sequence: " ++ name ++ "\nType: " ++ variation ++ "\nSequence: " ++ seq ++ "\nLength: " ++ show (seqLength (Sequence name seq variation)) ++ "\n"

instance Show Profile where
    show :: Profile -> String
    show (Prof matrix variation sequenceAmount name)  = "Profile: " ++ name ++ "\nType: " ++ variation ++ "\nSequences: " ++ show sequenceAmount ++ "\nMatrix: " ++ show matrix ++ "\n"

main :: IO ()
main = do
    -- Testing for the sequence part of the assignment begin here.
    let dnaMolecule = string2seq "TestDNA" "AAGTCCGTAGA"
    let dnaMolecule2 = string2seq "TestDNA2" "AAGTCAGTAGA"
    let dnaMolecule3 = string2seq "TestDNA3" "GGAGGGAGGAG"
    let proteinMolecule = string2seq "TestProtein" "AAGTCEGTAGA"
    let proteinMolecule2 = string2seq "TestProtein2" "AAETCAGTAEA"
    let proteinMolecule3 = string2seq "TestProtein3" "EEEEEEEEEEE"
    print dnaMolecule
    print dnaMolecule2
    print dnaMolecule3
    print proteinMolecule
    print proteinMolecule2
    print proteinMolecule3
    print ("Distance between " ++ name dnaMolecule ++ " and " ++ name dnaMolecule2 ++ ": " ++ show (seqDistance dnaMolecule dnaMolecule2))
    print ("Distance between " ++ name dnaMolecule ++ " and " ++ name dnaMolecule3 ++ ": " ++ show (seqDistance dnaMolecule dnaMolecule3))
    print ("Distance between " ++ name proteinMolecule ++ " and " ++ name proteinMolecule2 ++ ": " ++ show (seqDistance proteinMolecule proteinMolecule2))
    print ("Distance between " ++ name proteinMolecule ++ " and " ++ name proteinMolecule3 ++ ": " ++ show (seqDistance proteinMolecule proteinMolecule3))
    putStrLn ""
    -- Testing for the profile part of the assignment begin here.
    let dnaMolecule4 = string2seq "TestDNA4" "GGTACAGATAC"
    let dnaMolecule5 = string2seq "TestDNA5" "AGTTTACGATA"
    let dnaMolecule6 = string2seq "TestDNA6" "CCCCTAGGGAT"
    let dnaMolecule7 = string2seq "TestDNA7" "TTAGGACATAC"
    let dnaMolecule8 = string2seq "TestDNA8" "GGTATACATAC"
    let dnaMolecule9 = string2seq "TestDNA9" "CTTTAGAGGGG"
    let dnaProfile = molseqs2profile "DNA Profile 1" [dnaMolecule, dnaMolecule2, dnaMolecule3]
    let dnaProfile2 = molseqs2profile "DNA Profile 2" [dnaMolecule4, dnaMolecule5, dnaMolecule6]
    let dnaProfile3 = molseqs2profile "DNA Profile 3" [dnaMolecule7, dnaMolecule8, dnaMolecule9]
    let proteinMolecule4 = string2seq "TestProtein4" "GETACAIATDC"
    let proteinMolecule5 = string2seq "TestProtein5" "AGTTTEIGATE"
    let proteinMolecule6 = string2seq "TestProtein6" "CEECTAGDQAT"
    let proteinMolecule7 = string2seq "TestProtein7" "TTAGEECATAC"
    let proteinMolecule8 = string2seq "TestProtein8" "GGTAEEIQQAE"
    let proteinMolecule9 = string2seq "TestProtein9" "CEETAGQGDDQ"
    let proteinProfile = molseqs2profile "Protein Profile 1" [proteinMolecule, proteinMolecule2, proteinMolecule3]
    let proteinProfile2 = molseqs2profile "Protein Profile 2" [proteinMolecule4, proteinMolecule5, proteinMolecule6]
    let proteinProfile3 = molseqs2profile "Protein Profile 3" [proteinMolecule7, proteinMolecule8, proteinMolecule9]

    print dnaProfile
    print dnaProfile2
    print dnaProfile3
    print proteinProfile
    print proteinProfile2
    print proteinProfile3
    
    

    print (distanceMatrix [dnaMolecule, dnaMolecule2, dnaMolecule3])
    putStrLn ""
    print (distanceMatrix [proteinMolecule, proteinMolecule2, proteinMolecule3])
    putStrLn ""
    print (distanceMatrix [dnaProfile, dnaProfile2, dnaProfile3])
    putStrLn ""
    print (distanceMatrix [proteinProfile, proteinProfile2, proteinProfile3])

string2seq :: String -> String -> MolSeq
string2seq name seq = Sequence name seq (typeHelper seq)

seqName :: MolSeq -> String
seqName (Sequence name _ _) = name

seqSequence :: MolSeq -> String
seqSequence (Sequence _ seq _) = seq

seqLength :: MolSeq -> Int
seqLength (Sequence _ seq _) = length seq

seqType :: MolSeq -> String
seqType (Sequence _ seq _) = typeHelper seq

typeHelper :: String -> String
typeHelper seq
    | any (`notElem` "ACGT") seq = "Protein"
    | otherwise = "DNA"

seqDistance :: MolSeq -> MolSeq -> Double
seqDistance seq1 seq2 = distance
    where
        distance =
            let molType = seqType seq1
                isDNA = molType == "DNA"
                -- Alpha = andelen positioner där sekvenserna skiljer sig åt
                alpha = calculateDifference seq1 seq2

            in  
                if molType /= seqType seq2
                    then error "Invalid argument: Comparing DNA with Protein"
                    else let (const1, const2, const3) | isDNA = (3/4, 4, 3)
                                                      | otherwise = (19/20, 20, 19)

                             threshold = if isDNA then 0.74 else 0.4
                             result 
                                | alpha > threshold && isDNA = 3.3
                                | alpha > threshold = 3.7
                                | otherwise = -const1 * log (1 - (const2 * alpha / const3))
                         in if result == (-0.0) then 0.0 else result
calculateDifference :: MolSeq -> MolSeq -> Double
calculateDifference seq1 seq2 = calculateDifference' (seqSequence seq1) (seqSequence seq2) / fromIntegral (length (seqSequence seq1))
    where
        calculateDifference' :: String -> String -> Double
        calculateDifference' [] [] = 0
        calculateDifference' (x:xs) (y:ys) | x /= y = 1 + calculateDifference' xs ys
                                           | otherwise = calculateDifference' xs ys

nucleotides :: String
nucleotides = "ACGT"
aminoacids :: [Char]
aminoacids = sort "ARNDCEQGHILKMFPSTWYV"
makeProfileMatrix :: [MolSeq] -> [[(Char, Int)]]
makeProfileMatrix [] = error "Empty sequence list"
makeProfileMatrix sl = res
    where
        t = seqType (head sl) -- Sparar typen av Molseq, dvs DNA eller Protein
        defaults =
            if t == "DNA" then
                zip nucleotides (replicate (length nucleotides) 0) -- Om typen är DNA, matcha alla nucleotides (ACTG) med nollor i tuplar, dvs [('A', 0), ('C', 0), ('G', 0), ('T', 0)], och sparar detta som defaults.
            else
                zip aminoacids (replicate (length aminoacids) 0) -- Om typen är Protein, utför samma process som ovan fast för alla aminoacids (ARNDCEQGHILKMFPSTWYV) och spara som tuplar med 0.
        seqs = map seqSequence sl -- Kör funktionen seqSequence på varje element i s1 och sparar som ny lista, seqs, vilken alltså blir en lista med alla sekvenser som strängar.
        transp = transpose seqs {- Om vi ser på vår lista av sekvenser som en matris (då strängarna i sig är listor), så transponerar detta rader och kolumner. 
        Det innebär att vi kommer få en ny lista med de första elementen i varje string ur den urprungliga listan, som den första string:en i den nya listan, de andra elementen i varje string som den andra stringen osv. -}
        func x = (head x, length x) -- Tar en lista och returnerar en tuple som innehåller det första elementet i listan, samt listans längd. Exempelvis blir [5, 2, 3, 4] till (5, 4)
        sortAndGroup = map func . group . sort {- Tar en lista, och sorterar den först. I vårt fall kommer detta vara string:s med alla elementen som ursprungligen var på en viss position, dvs de string:s som skapades av transponeringen.
        Funktionen group grupperar sedan vidare de intilliggande element som är lika i ytterligare sublistor. Exempelvis "AAG" = ['A', 'A', 'G'] -> [['A', 'A'], ['G']] = ["AA", "G"].
        Funktionen func körs sedan på varje lista i detta resultat, vilket returnerar en tuple med de första element i listan, samt längden på listan. För exempel ovan får vi då ["AA", "G"] -> [('A', 2), ('G', 1)]. -}
        elementsByPosition = map sortAndGroup transp {- Kör den sekvens av funktioner som beskrivs ovan på transponatet, exempel på resultat: 
        ["AGTA", "AGGT", "GAGT"] -> ["AAG", "GGA", "TGG", "ATT"] -> ["AAG", "AGG", "GGT", "ATT"] -> [["AA", "G"], ["A", "GG"], ["GG", "T"], ["A", "TT"]] -> [[('A', 2), ('G', 1)], [('A', 1), ('G', 2)], [('G', 2), ('T', 1)], [('A', 1), ('T', 2)]]
        Detta ger oss alltså en lista, som genom sublistor av tuplar säger hur många av varje element som befinner sig på varje position. [('A', 2), ('G', 1)] betyder till exempel att på den första positionen i alla sekvenserna finns 2 A och 1 G, osv. -}
        equalFst a b = fst a == fst b -- Funktion som returnerar true eller false beroende på om de första elementen i tuple a och tuple b är lika.
        combineTuples list = unionBy equalFst list defaults -- Kombinerar tuplarna i defaults, med tuplarna i den försedda list, då det första elementet i tuplarna, i vårt fall bokstaven, är samma.
        res = map (sort . combineTuples) elementsByPosition {- Kör combineTuples samt sorterar resultatet på varje lista i elementsByPosition, sorteringsex: [('A',2),('G',1),('C',0)] blir [('A',2),('C',0),('G',1)].
        Resultatet blir nu en lista av listor (matris) av tuples, som säger oss hur många ev ett visst element / viss bokstav som finns på en viss position (exempelvis första bokstaven) i alla sekvenserna vi började med. -}
        

molseqs2profile :: String -> [MolSeq] -> Profile
molseqs2profile name seqs = prof
    where
        matrix = makeProfileMatrix seqs
        sequenceAmount = length seqs
        variation = seqType (head seqs)
        prof = Prof matrix variation sequenceAmount name

profileName :: Profile -> String
profileName (Prof _ _ _ name) = name

profileFrequency :: Profile -> Int -> Char -> Double
profileFrequency (Prof matrix _ _ _) pos char = findFrequency (matrix !! pos) -- Tar listan på position pos i matrisen matrix
    where
        findFrequency :: [(Char, Int)] -> Double
        findFrequency [] = error "Character not part of any sequence!"
        findFrequency ((c, freq):xs) -- Går igenom varje tuple i listan
            | c == char = fromIntegral freq -- Om bokstaven stämmer med char så tar den det andra värdet ur tuplen och returnerar det som frekvensen
            | otherwise = findFrequency xs -- Annars fortsätter den att rekursivt gå igenom alla tuples

profileDistance :: Profile -> Profile -> Double
profileDistance (Prof matrix1 variation1 _ _) (Prof matrix2 variation2 _ _)
    | variation1 /= variation2 = error "Profiles are of different molecule types"
    | otherwise = distance
    where
        sumTuples :: [(Char, Int)] -> [(Char, Int)] -> Double
        sumTuples [] [] = 0.0
        --Eftersom våra listor är sorterade behöver vi inte tänka på vilken karaktär vi är vid, de kommer alltid att vara samma över båda listorna.
        sumTuples ((_, freq1):xs1) ((_, freq2):xs2) = abs (fromIntegral (freq1 - freq2)) + sumTuples xs1 xs2

        sumFrequency :: [[(Char, Int)]] -> [[(Char, Int)]] -> Double
        sumFrequency [] [] = 0.0
        sumFrequency (x1:xs1) (x2:xs2) = sumTuples x1 x2 + sumFrequency xs1 xs2

        distance = sumFrequency matrix1 matrix2

        