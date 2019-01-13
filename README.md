## Arthas

Fork from [arthas](https://github.com/alibaba/arthas), you could read the full description in the original repo.


### New Features

* `ls` linux command supported

  `` ls -lrt -d -1 /home/admin/* ``
  
  
* nested linux command in redefine command supported ,sometimes we need linux command pipeline to specify redefine path.

  ``redefine -p `ls -lrt -d -1 /home/admin/* | grep class ``

### Attention

code tested only on mac  10.13.3