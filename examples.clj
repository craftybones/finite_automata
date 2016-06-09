[{
  :name "odd number of zeroes"
  :type "dfa"
  :tuple {
          :states #{:q1 :q2}
          :alphabets #{"0" "1"}
          :delta {:q1 {0 :q2 1 :q1} :q2 {0 :q1 1 :q2}}
          :start-state :q1
          :final-states #{:q2}
          }
  :pass-cases ["0"
               "000"
               "00000"
               "10"
               "101010"
               "010101"]
  :fail-cases ["00"
               "0000"
               "1001"
               "1010"
               "001100"]
  }
 {
  :name "even number of zeroes"
  :type "dfa"
  :tuple {
          :states #{:q1 :q2}
          :alphabets #{"0" "1"}
          :delta {:q1 {0 :q2 1 :q1} :q2 {0 :q1 1 :q2}}
          :start-state :q1
          :final-states #{:q1}
          }
  :fail-cases ["0"
               "000"
               "00000"
               "10"
               "101010"
               "010101"]
  :pass-cases ["00"
               "0000"
               "1001"
               "1010"
               "001100"]
  }
 {
  :name "at least one zero"
  :type "dfa"
  :tuple {
          :states #{:q1 :q2}
          :alphabets #{"0" "1"}
          :delta {:q1 {0 :q2 1 :q1} :q2 {0 :q2 1 :q2}}
          :start-state :q1
          :final-states #{:q2}
          }
  :pass-cases ["0"
               "10"
               "100"
               "1100"
               "01"
               "010"]
  :fail-cases [""
               "1"
               "11"
               "111"]
  }
 {
  :name "at least one one"
  :type "dfa"
  :tuple {
          :states #{:q1 :q2}
          :alphabets #{"0" "1"}
          :delta {:q1 {1 :q2 0 :q1} :q2 {0 :q2 1 :q2}}
          :start-state :q1
          :final-states #{:q2}
          }
  :pass-cases ["1"
               "10"
               "100"
               "1100"
               "01"
               "010"]
  :fail-cases [""
               "0"
               "00"
               "000"]
  }
 {
  :name "string length multiple of three"
  :type "dfa"
  :tuple {
          :states #{:q1 :q2 :q3}
          :alphabets #{"0" "1"}
          :delta {:q1 {0 :q2 1 :q2}
                  :q2 {0 :q3 1 :q3}
                  :q3 {0 :q1 1 :q1}}
          :start-state :q1
          :final-states #{:q1}
          }
  :pass-cases ["000"
               "111"
               "010"
               "101"
               "111111"
               "000000"
               "101010"
               "010101"]
  :fail-cases ["00"
               "11"
               "10"
               "01"
               "11111"
               "00000"
               "01010"
               "10101"]
  }
 {
  :name "alternate ones and zeroes beginning with zero"
  :type "dfa"
  :tuple {
          :states #{:q1 :q2 :q3 :q4}
          :alphabets #{"0" "1"}
          :delta {:q1 {0 :q2 1 :q4}
                  :q2 {0 :q4 1 :q3}
                  :q3 {0 :q2 1 :q4}
                  :q4 {0 :q4 1 :q4}}
          :start-state :q1
          :final-states #{:q2 :q3}
          }
  :pass-cases ["0"
               "01"
               "010"
               "0101"
               "01010"
               "010101"]
  :fail-cases [""
               "1"
               "10"
               "101"
               "11"
               "00"
               "0100"
               "011"]
  }
 {
  :name "alternate characters beginning and ending with same letter"
  :type "nfa"
  :tuple {
          :states #{:q1 :q2 :q3 :q4 :q5 :q6 :q7}
          :alphabets #{"0" "1"}
          :delta {:q1 {:e #{:q2 :q5}}
                  :q2 {0 #{:q3}}
                  :q3 {1 #{:q4}}
                  :q4 {0 #{:q3}}
                  :q5 {1 #{:q6}}
                  :q6 {0 #{:q7}}
                  :q7 {1 #{:q6}}
                  }
          :start-state :q1
          :final-states #{:q3 :q6}
          }
  :pass-cases ["0"
               "010"
               "01010"
               "1"
               "101"
               "10101"]
  :fail-cases [""
               "10"
               "01"
               "11"
               "00"
               "001"
               "100"
               "1100"]
  }
 {
  :name "either even number of zeroes or even number of ones"
  :type "nfa"
  :tuple {
     				 :states #{:q1 :q2 :q3 :q4 :q5}
     				 :alphabets #{"0" "1"}
     				 :delta {:q1 {:e #{:q2 :q4}}
         								 :q2 {0 #{:q3} 1 #{:q2}}
         								 :q3 {0 #{:q2} 1 #{:q3}}
         								 :q4 {1 #{:q5} 0 #{:q4}}
         								 :q5 {1 #{:q4} 0 #{:q5}}
         								 }
     				 :start-state :q1
     				 :final-states #{:q2 :q4}
     				 }
  :pass-cases ["00"
        			 "0000"
        			 "0101010"
        			 "00010"
        			 "11"
        			 "1111"
               "110101"
               "10101010"]
  :fail-cases ["0001"
        			 "1110"
        			 "111000"
							 "01"
							 "10"
							 "000111"]
  }
 {
  :name "sparse zero sandwich - any number of 1s with utmost one zero"
  :type "nfa"
  :tuple {
     				 :states #{:q1 :q2}
     				 :alphabets #{"0" "1"}
     				 :delta {:q1 {:e #{:q2} 0 #{:q2} 1 #{:q1}}
         						 :q2 {1 #{:q2}}
         						}
     				 :start-state :q1
     				 :final-states #{:q2}
     				 }
  :pass-cases ["1"
							 "11"
            	"101"
            	"110"
            	"01"
            	"011"
               "1111"]
  :fail-cases ["00"
               "010"
               "100"
               "110011"
               "1010"]
  }
 {
  :name "any number of zeroes followed by any number of ones"
  :type "nfa"
  :tuple {
     				 :states #{:q1 :q2}
     				 :alphabets #{"0" "1"}
     				 :delta {:q1 {:e #{:q2} 0 #{:q1}}
           					 :q2 {1 #{:q2}}
            				}
     				 :start-state :q1
     				 :final-states #{:q2}
     				 }
  :pass-cases [""
               "0"
               "1"
               "00"
               "001"
               "0011"
               "0001"
               "011"
               "000111"]
  :fail-cases ["10"
               "1110"
               "010"
               "10101"
               "1101"]
  }
 {
  :name "0*1* or 1*0*"
  :type "nfa"
  :tuple {
          :states #{:q1 :q2 :q3 :q4 :q5}
          :alphabets #{"0" "1"}
          :delta {:q1 {:e #{:q2 :q4}}
                  :q2 {0 #{:q2} :e #{:q3}}
                  :q3 {1 #{:q3}}
                  :q4 {1 #{:q4} :e #{:q5}}
                  :q5 {0 #{:q5}}
                  }
          :start-state :q1
          :final-states #{:q3 :q5}
          }
  :pass-cases [""
               "0"
               "1"
               "00"
               "11"
               "001"
               "110"
               "011"
               "100"
               "0011"
               "1100"]
  :fail-cases ["101"
               "010"
               "11001"
               "00110"
               "0101"
               "1010"]
  }
 ]
