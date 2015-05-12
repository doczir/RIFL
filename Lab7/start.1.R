require(ggplot2)
require(BSDA)
require(reshape2)

## 3. feladat
################################################################################
activities <- read.csv("data_1000_egyenletes.csv", header = T, sep = ";")
# activities <- read.csv("data_5000_egyenletes.csv", header = T, sep = ";")
# activities <- read.csv("data_10000_egyenletes.csv", header = T, sep = ";")
# activities <- read.csv("data_1000_normal.csv", header = T, sep = ";")
# activities <- read.csv("data_5000_normal.csv", header = T, sep = ";")
# activities <- read.csv("data_10000_normal.csv", header = T, sep = ";")
melted <- melt(activities,
               variable.name = "Activity", value.name = "Delay")
ggplot(melted) + geom_histogram(aes(x = Delay)) + facet_grid(Activity ~ .)
################################################################################
## Várható érték és szórás kiszámítása a BillingInfoNode-hoz a megadott csv alapján
activities <- read.csv("data_10000_normal.csv", header = T, sep = ";")
m = sum(activities$BillingInfoNode) / length(activities$BillingInfoNode)
s = sqrt(sum((activities$BillingInfoNode - m)^2) / length(activities$BillingInfoNode))
################################################################################
## Centrális határeloszlás tétele ; 10000 minta - normál eloszlás
activities <- read.csv("data_10000_normal.csv", header = T, sep = ";")
rowAvgs = rowSums(activities, na.rm = T) / 7
m = sum(rowAvgs[1:50]) / 50
s = sd(rowAvgs[1:50])
################################################################################
## Centrális határeloszlás tétele ; 10000 minta - egyenletes eloszlás
activities <- read.csv("data_10000_egyenletes.csv", header = T, sep = ";")
rowAvgs = rowSums(activities, na.rm = T) / 7
m = sum(rowAvgs[1:50]) / 50
s = sd(rowAvgs[1:50])
################################################################################

## 5. feladat
################################################################################
rowAvgs = rowSums(activities, na.rm = T) / 7
m = sum(rowAvgs) / length(rowAvgs)     #várható érték (998.8)
s = sqrt(sum((rowAvgs - m)^2) / length(rowAvgs))   #szórás(37.3)
qplot(rowAvgs, geom = "histogram")

u = z.test(rowAvgs, mu=100, sigma.x=37)

u = z.test(rowAvgs, mu=998, sigma.x=37)
################################################################################

## 6. feladat
################################################################################

activity_5k <- read.csv("data_5000_egyenletes.csv", header = T, sep = ";")
activity_10k <- read.csv("data_10000_egyenletes.csv", header = T, sep = ";")
qqplot(activity_5k$BillingInfoNode, activity_10k$BillingInfoNode)

################################################################################

## 8. feladat
################################################################################

activities <- read.csv("artif.csv", header = T, sep = ",")

## a.)

melted <- melt(activities,
               variable.name = "Activity", value.name = "Delay")
ggplot(melted) + geom_histogram(aes(x = Delay)) + facet_grid(Activity ~ .)

data= activities$ACT1[!is.na(activities$ACT1)]
m = sum(data) / length(data)    	
s = sqrt(sum((data - m)^2) / length(data))

data= activities$ACT2[!is.na(activities$ACT2)]
m = sum(data) / length(data)      
s = sqrt(sum((data - m)^2) / length(data))

data= activities$ACT3[!is.na(activities$ACT3)]
m = sum(data) / length(data)      
s = sqrt(sum((data - m)^2) / length(data))

data= activities$ACT4[!is.na(activities$ACT4)]
m = sum(data) / length(data)      
s = sqrt(sum((data - m)^2) / length(data))

data= activities$ACT5[!is.na(activities$ACT5)]
m = sum(data) / length(data)      
s = sqrt(sum((data - m)^2) / length(data))

data= activities$ACT6[!is.na(activities$ACT6)]
m = sum(data) / length(data)      
s = sqrt(sum((data - m)^2) / length(data))

data= activities$ACT7[!is.na(activities$ACT7)]
m = sum(data) / length(data)      
s = sqrt(sum((data - m)^2) / length(data))

## b.)

chisq.test(table(activities$ACT1))

## c.)

chisq.test(table(activities$ACT7))

## d.)

qqplot(activities$ACT2, activities$ACT3)
qqplot(activities$ACT5, activities$ACT8)

## f.)

qplot(rowSums(activities, na.rm = T), geom = "histogram")

################################################################################
