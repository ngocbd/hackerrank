df=read.csv("https://s3.amazonaws.com/hr-testcases/399/assets/trainingdata.txt",header = FALSE,col.names = c("charged","lasted"))
head(df)
library(lattice)
model1 = lm(lasted ~ charged , data = df)
summary(model1)
xyplot(charged ~ lasted, data = df,
       xlab = "lasted",
       ylab = "charged",
       main = "Baterry life when charged",
       panel = function(x, y) {
         panel.xyplot(x, y)
         panel.abline(lm(y ~ x))
       }
)

predict(model1, newdata=data.frame(charged=8))
