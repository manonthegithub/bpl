
require 'yaml'

current_dir    = File.dirname(File.expand_path(__FILE__))
configs        = YAML.load_file("#{current_dir}/web/target/vagrantConfigWeb.yaml")

Vagrant.configure("2") do |config|

  inner_db_port = 5432
  db_port = configs['postgres_port']
  db_pass = configs['postgres_pass']

  config.vm.box = "ubuntu/trusty64"

  config.vm.network "forwarded_port", guest: inner_db_port, host: db_port

  config.vm.provision "docker" do |d|
        d.pull_images "postgres:9.5.5"
        d.run "postgres",
            args: "--expose #{inner_db_port} -p=#{inner_db_port}:#{inner_db_port} --name pg -e POSTGRES_PASSWORD=#{db_pass} -d"
  end

end
