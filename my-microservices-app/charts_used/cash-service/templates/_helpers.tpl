{{/*
Expand the name of the chart.
*/}}
{{- define "cash-service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "cash-service.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{- define "cash-service.shortname" -}}
{{- $full := include "cash-service.fullname" . -}}
{{- $prefix := printf "%s-" .Release.Name -}}
{{- $trimmedPrefix := trimPrefix $prefix $full -}}
{{- $shortname := trimSuffix "-service" $trimmedPrefix -}}
{{- $shortname }}
{{- end }}


{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "cash-service.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "cash-service.labels" -}}
helm.sh/chart: {{ include "cash-service.chart" . }}
{{ include "cash-service.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "cash-service.selectorLabels" -}}
app.kubernetes.io/name: {{ include "cash-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "cash-service.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "cash-service.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }} 