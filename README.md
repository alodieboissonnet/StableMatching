# StableMatching
Implementation of Gale-Shapley algorithm for Stable Matching in Java

In the considered variant of the stable matching problem, there are n men and n women. Each man is to marry one woman and each woman is to marry one man. Men are divided into m groups and similarly women are divided into w groups, with groups being possibly of di↵erent sizes. The sizes of all groups are given. Within each group, all individuals are alike: they are all equally attractive in the eyes of any person of the other gender, and they all have identical preference ordering with respect to groups of the other gender. The problem is essentially the same as the original stable matching task, but it allows us to represent preference orderings more compactly by a pair of matrices of dimensions m ⇥ w and w ⇥ m (representing the ordering of preferences for the groups of men and women, respectively) instead of a pair of n ⇥ n matrices (representing the ordering of preferences between individuals).

A matching is considered stable if there does not exist a man and a woman who would both (strictly) prefer to be with each other than with the partner they are currently married to. (It is sometimes said that such a matching is “weakly” stable.) The output of your program should be an integer matrix M of dimension m ⇥ w, with entry Mi,j denoting the number of men from the i-th group of men married to women from the j-th group of women.


Case A : max{m, w} < 104 and n < 109 . A runtime complexity of O(n2 ) will not be su cient to pass all tests. You can still apply a variant of the Gale-Shapley algorithm on the group preference matrix, but with some optimizations. For example, try the approach below.

Case B : n < 104. You can expand all groups into individuals, creating a preference matrix of dimension n ⇥ n. You can then apply the Gale-Shapley algorithm from the lecture to this matrix, achieving O(n2) complexity.
